from Bio import SeqIO
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from joblib import load
import pickle
import tensorflow as tf


def read_file_fasta(file_name) :
  it = SeqIO.parse(file_name, 'fasta')
  rows = []
  while True:
    try:
        seqRecord = next(it)
        rows.append(str(seqRecord.seq).lower())
    except StopIteration:
        break

  return rows
def getET(file_name):
  it = SeqIO.parse(file_name, 'fasta')
  row = []
  while True:
    try:
        seqRecord = next(it)
        row.append(">"+seqRecord.description+"\n"+str(seqRecord.seq).lower()+"\n")
    except StopIteration:
        break

  return row



def get_genome_without_scafold(genome_file) :
    file = open(r'genome_without_scafold.txt', 'w')
    genome = ""
    it = SeqIO.parse(genome_file, 'fasta')
    while True:
        try:
            seqRecord = next(it)
            genome = genome + str(seqRecord.seq).lower()
        except StopIteration:
            break
    file.write(genome)

#retourne le complémentaire d'une séquence
def get_comp_seq(seq) :
    new_seq=[]
    for nucliotide in seq :
        if(nucliotide == "a") :
            new_seq.append("t")
        elif(nucliotide == "t") :
            new_seq.append("a")
        elif (nucliotide == "c") :
            new_seq.append("g")
        elif (nucliotide == "g") :
            new_seq.append("c")
    return "".join(new_seq)

#diviser le genome en sequences de 50000 nucléotides
# def div_seqs(genome) :
#     L = []
#     for i in range(0, len(genome), 48400):
#         L.append(genome[i:i + 50000])

#     return L

#construire une map contenant les tir de 30 nucliéotides
def get_map(genome) :
    d = {}
    for i in range(0, len(genome) - 29):
            d[i] = genome[i:i+30]
    return d

#construire une map pour chaque séquence de 50000 nucliéotides
def get_map_seqs(list_seqs) :
    list_map = []
    d = {}
    for i in list_seqs :
        d = get_map(i)
        list_map.append(d)
    return list_map

#retourne les mutations possible d'un tir dans une position spécifique
def get_mutation(tir, i) :
    if (tir[i] == 'a') :
       tir[i] = 't'
       m1 = "".join(tir)
       tir[i] = 'c'
       m2 = "".join(tir)
       tir[i] = 'g'
       m3 = "".join(tir)
       return m1, m2, m3
    elif (tir[i] == 't') :
        tir[i] = 'a'
        m1 = "".join(tir)
        tir[i] = 'c'
        m2 = "".join(tir)
        tir[i] = 'g'
        m3 = "".join(tir)
        return m1, m2, m3
    elif (tir[i] == 'c') :
        tir[i] = 't'
        m1 = "".join(tir)
        tir[i] = 'a'
        m2 = "".join(tir)
        tir[i] = 'g'
        m3 = "".join(tir)
        return m1, m2, m3
    elif (tir[i] == 'g') :
        tir[i] = 't'
        m1 = "".join(tir)
        tir[i] = 'c'
        m2 = "".join(tir)
        tir[i] = 'a'
        m3 = "".join(tir)
        return m1, m2, m3

#retourne tous les mutations possible pour un tir dans un seul nucléotide (une seule mutation)
def get_tir_mutations(tir) :
    t = list(tir)
    liste_muts = []
    liste_muts.append(tir)
    for i in range(0, len(t)) :
        t = list(tir)
        m1, m2, m3 = get_mutation(t, i)
        liste_muts.extend([m1, m2, m3])
    return liste_muts

#retourne une liste des mutations possibles d'un tir au niveau de deux nucléotides (deux mutations)
def get_tir_two_mutations(tir) :
    t = list(tir)
    liste_muts = []
    liste_muts.append(tir)
    for i in range(0, len(t)):
        t = list(tir)
        m1, m2, m3 = get_mutation(t,i)
        liste_muts.extend([m1, m2, m3])
        for j in range(i+1, len(t)) :
            mt1, mt2, mt3 = get_mutation(list(m1), j)
            liste_muts.extend([mt1, mt2, mt3])
            mt1, mt2, mt3 = get_mutation(list(m2), j)
            liste_muts.extend([mt1, mt2, mt3])
            mt1, mt2, mt3 = get_mutation(list(m3), j)
            liste_muts.extend([mt1, mt2, mt3])
    return liste_muts

#retourne la position de mutation
def get_num_mut(org, mut) :
    mutation_num = []
    for j in range(0, len(org)) :
        try:
            if (org[j] != mut[j]) :
                mutation_num.append(j)
        except Exception as e:
             pass
    if len(mutation_num) == 0 :
        mutation_num.append('None')
        mutation_num.append('None')
    elif len(mutation_num) == 1 :
        mutation_num.append('None')
    return mutation_num

def get_tirs(d, genome,s) :
    TIRs_file_TSD = open("ETs_TSD.txt", 'w')
    TIRs_file_withoutTSD = open("ETs_withoutTSD.txt", 'w')
    for i in range(0, len(d)) :
        for j in range(i + 1, len(d)) :
                 if(getSimilarity(d[i], "".join(reversed(get_comp_seq(d[j])))) >= s) :
                        if(genome[i-2:i] == genome[j + len(d[j]):j + len(d[j]) + 2]) and (genome[i - 2:i] != "" and genome[j + len(d[j]):j + len(d[j]) + 2] != "") :
                                if(len(genome[i-2:i] + d[i] + genome[i+len(d[i]):j] + d[j] + genome[j+len(d[j]):j+len(d[j])+2])>=46 and len(genome[i-2:i] + d[i] + genome[i+len(d[i]):j] + d[j] + genome[j+len(d[j]):j+len(d[j])+2])<=1291) :
                                    print("--with tsd--")
                                    TIRs_file_TSD.write("> Tir 1 : " + d[i]  + " | Tir 2 : " + d[j] + " | Mutations : " + str(get_muts(d[i],"".join(reversed(get_comp_seq(d[j]))),i)) + " | Similarity : " + str(getSimilarity(d[i], "".join(reversed(get_comp_seq(d[j]))))) + "\n")
                                    TIRs_file_TSD.write(genome[i-2:i] + d[i] + genome[i+len(d[i]):j] + d[j] + genome[j+len(d[j]):j+len(d[j])+2] + '\n')
                        else :
                                if(len(genome[i-2:i] + d[i] + genome[i+len(d[i]):j] + d[j] + genome[j+len(d[j]):j+len(d[j])+2])>=46 and len(genome[i-2:i] + d[i] + genome[i+len(d[i]):j] + d[j] + genome[j+len(d[j]):j+len(d[j])+2])<=1291) :
                                    print("--without tsd--")
                                    TIRs_file_withoutTSD.write("> Tir 1 : " + d[i] + " | Tir 2 : " + d[j] + " | Mutations : " + str(get_muts(d[i],"".join(reversed(get_comp_seq(d[j]))),i)) + " | Similarity : " + str(getSimilarity(d[i], "".join(reversed(get_comp_seq(d[j]))))) + "\n")
                                    TIRs_file_withoutTSD.write(d[i] + genome[i+len(d[i]):j] + d[j] + '\n')



#construire une liste des éléments transposables
def get_ETs_from_file(file_name) :
    it = SeqIO.parse(file_name, 'fasta')
    list_ETs = []
    while True:
        try:
            seqRecord = next(it)
            list_ETs.append(str(seqRecord.seq))
        except StopIteration:
            break
    return list_ETs

#retourne la longueur maximale entre deux séquences
def max_len(seq1, seq2):
    max_len = max(len(seq) for seq in [seq1, seq2])
    return max_len

#unifier la longueur entre deux séquences
def get_unified_len(seq1, seq2):
    maxLen = max_len(seq1, seq2)
    seq_res1 = list(seq1)
    seq_res2 = list(seq2)
    if (len(seq1) < maxLen):
        for i in range(0, maxLen - len(seq1)):
            seq_res1.append('z')
    if (len(seq2) < maxLen):
        for i in range(0, maxLen - len(seq2)):
            seq_res2.append('z')
    return "".join(seq_res1), "".join(seq_res2)

#mesurer la similarité entre deux séquences
def get_sim_two_seq(seq1, seq2) :
    som = 0
    seq1_unified_len, seq2_unified_len = get_unified_len(seq1, seq2)
    for i in range(0, len(seq1_unified_len)) :
        if seq1_unified_len[i] == seq2_unified_len[i] :
            som = som+1
    return som/max_len(seq1, seq2)

#mesurer la similarité entre une séquence et une liste de séquences
def get_simmilarity(seq) :
    list_ETs = get_ETs_from_file('DataSet/ET/TC1_MARINER.txt')
    list_sim = []
    for sq in list_ETs :
        list_sim.append(get_sim_two_seq(seq, sq))
    print(max(list_sim))
    if max(list_sim) <= 0.24 :
        return False
    else :
        return True

def get_Non_tirs(d, genome) :
    ENT = ''
    ENTs = open("ENTs.txt",'w')
    for i in range(0, len(d), 150):
            for j in range(i + 60, len(d), 120) :
                    if (d[i] != "".join(reversed(get_comp_seq(d[j])))) :
                            print("ok 1")
                            ENT = d[i]+genome[i+len(d[i]):j]+d[j]
                            if((len(ENT) >= 76 and len(ENT) <= 21484) and (get_simmilarity(ENT) == False)) :
                                     print("ok 2")
                                     ENTs.write(">"+ ENT + "\n")


def get_number_mutations_tc1(seqs) :
    list_num_muts = [len(get_num_mut(seq[2:32], "".join(reversed(get_comp_seq(seq[-32:-2]))))) for seq in seqs]
    return min(list_num_muts), max(list_num_muts)

def getSimilarity(seq1, seq2):
    s = 0
    for i in range(0, len(seq1)) :
        if(seq1[i] == seq2[i]) :
            s = s + 1
    return (s*100)/len(seq1)

def get_muts(seq1, seq2, index) :
    l = []
    for i in range(0, len(seq1)):
        if (seq1[i] != seq2[i]):
            #print(seq1[i],"***",seq2[i],"***",getSimilarity(seq1, seq2))
            l.append(index+i+1)
    return l
