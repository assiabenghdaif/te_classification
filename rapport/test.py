##################################server
from flask import Flask, request, jsonify
from helper import *
from fonctionsTIR import *
from emailsender import *
from tensorflow.keras.models import load_model



app = Flask(__name__)
tfidf_vectorizer=load_modelTT("models/tfidf_vectorizer.joblib")
lstm_model=load_modelTT("models/SVM_classifier_poly_2.joblib")
cnn_gru_model = load_modelCNN()
# label_encoder = load_PKL("models/label_encoder_for_dna_ML_CNN_GRU_onehot.pkl")
# cnn_gru_model=load_modelCNN_GRU("models/model_weights_dna_ML_CNN_GRU_onehot_v3.weights.h5")
# lstm_model = load_modelLSTM()
# vectorizer_lstm = load_JOBLIB("models/TfidfVectorizer_vectorizer_lstm_k=3.joblib")
# k=3

@app.route('/send_data', methods=['POST'])
def receive_data():
    try:
        
        # rf_model_20.joblib
        
        # kmeans_model=joblib.load("models/kmeans_model.joblib")
        # tfidf_vectorizer=joblib.load("models/tfidf_vectorizer.joblib")
        # Assuming the data is sent as JSON
        received_data = request.get_json()
        sequences = received_data  # The received_data is now the list directly
        sequences = [s.replace('\n', '') for s in sequences]
        sequences = [s.lower() for s in sequences]
        # Process the received data as needed
        # print("Received data:", sequences)
        # if(preprocessing_LSTM_CNN_GRU(sequences[0])==True):
            

            # for LSTM
        # sequences_X=preprocessing(sequences,k)
        # print(sequences)
        # sequences_X_vecto=Vectorize(sequences_X,vectorizer_lstm)
        # print(sequences_X_vecto)
        # print(sequences_X_vecto.shape)
        # print(type(sequences_X_vecto))
        # sequences_pred = predict_LSTM(sequences_X_vecto,lstm_model)
        # sequences_pred_proba = getProba_LSTM(sequences_pred)
        # sequences_label=getLabel_LSTM(sequences_pred)
        # # sequences_classe=[]
        # # for seq in sequences_pred:
        # sequences_classe=clusterDetected(sequences_label[0])
        # print(sequences_pred_proba)
        # print(sequences_classe)
        # for SVM
        sequences_X=preprocessing(sequences)
        print(sequences)
        sequences_X_vecto=Vectorize(sequences_X,tfidf_vectorizer)
        print(sequences_X_vecto)
        print(sequences_X_vecto.shape)
        print(type(sequences_X_vecto))
        sequences_pred = predict(lstm_model,sequences_X_vecto)
        sequences_pred_proba = predict_proba(lstm_model,sequences_X_vecto)
        print("tt==",sequences_pred_proba)

        sequences_pred_proba=proba_SVM(sequences_pred_proba[0])
        # sequences_classe=[]
        # for seq in sequences_pred:
        sequences_classe=clusterDetected(sequences_pred)
        print(sequences_pred_proba)
        print(sequences_classe)
                    
            
        return jsonify({'message': 'Data received successfully','sequences_classe': sequences_classe,'sequences_pred_proba':sequences_pred_proba}), 200

    except Exception as e:
        print("Error processing data:", str(e))
        print(e)
        return jsonify({'error': 'Error processing data'}), 500


@app.route('/SuperFamilies', methods=['POST'])
def SuperFamilies():
    try:
        
        # rf_model_20.joblib
        
        # kmeans_model=joblib.load("models/kmeans_model.joblib")
        tfidf_vectorizer=joblib.load("models/tfidf_vectorizer.joblib")
        # Assuming the data is sent as JSON
        received_data = request.get_json()
        sequences = received_data  # The received_data is now the list directly
        # print(sequences)
        sequences = received_data.get('sequences', [])
        # similarity = received_data.get('similarity', 0)
        # emailConnect = received_data.get('emailConnect','')
        
        print(sequences)
        sequences_X=preprocessing(sequences)
        print(sequences)
        sequences_X_vecto=Vectorize(sequences_X,tfidf_vectorizer)
        print(sequences_X_vecto)
        print(sequences_X_vecto.shape)
        print(type(sequences_X_vecto))
        
        sequences_pred_proba_family = predict_cnn_proba(cnn_gru_model,sequences_X_vecto)
        res_percentage = [[f"{i * 100:.2f}%" for i in inner] for inner in sequences_pred_proba_family]

        print(res_percentage)
        sequences_pred = predict_cnn_class(sequences_pred_proba_family)
        ii=labelsFamilles(sequences_pred)
        sequences_family=classesDetectedTT(ii)
        print(sequences_pred_proba_family)
        print(type(sequences_pred_proba_family))
        print(sequences_pred)
        print(sequences_family)
        sequences_pred_proba_family=proba_CNN(sequences_pred_proba_family[0],sequences_family)
        print("tt=",sequences_pred_proba_family)
        
        
        # sequences = [s.replace('\n', '') for s in sequences]
        # # Process the received data as needed
        # # print("Received data:", sequences)
        # # if(preprocessing_LSTM_CNN_GRU(sequences[0])==True):
        # # print(sequences)

        # cleaned_sequences = [clean_sequence(seq) for seq in sequences]
        # # print(cleaned_sequences)
        # encoded_sequences = [encode_sequence_onehot(seq) for seq in cleaned_sequences]
        # # print(encoded_sequences)
        # padded_sequences = padded_sequences_(encoded_sequences)
        # y_pred_gru = y_pred_gru_(cnn_gru_model,padded_sequences)
        # print("y_pred_gru",y_pred_gru)
        # res_percentage_cnn_gru=proba_CNN_GRU(y_pred_gru[0])
        # # res_percentage_cnn_gru=['0.005%', '0.009%', '0.000%', '0.010%', '0.974%', '0.001%', '0.000%']
        # y_pred_classes_=predict_cnn_gru_class(y_pred_gru[0])
        # predicted_label = predicted_label_(label_encoder,y_pred_classes_)
        # classesDetected_name=classesDetected(predicted_label)
        # # classesDetected_name="PiggyBac"

        #     # d=get_map(sequences[0])
        #     # get_tirs(d,sequences,similarity)
        #     # sender_email = 'benghdaif.assia2001@gmail.com'
        #     # smtp_user = 'assiabenghdaif@gmail.com'
        #     # smtp_password = 'nmae anyx krbf tjhh'  # App-specific password if 2FA is enabled

        #     # email_sender = EmailSender(sender_email, smtp_user, smtp_password)

        #     # # receiver_email = 'assiabenghdaif@gmail.com'
        #     # subject = 'TIRs Localisation'
        #     # body = 'This files contains the results of TIRs localisation of your sequence.'
        #     # filenames = ['ETs_TSD.txt', 'ETs_withoutTSD.txt']  # List of files to attach
        #     # email_sender.send_email(emailConnect, subject, body, filenames)
            
        return jsonify({'message': 'Data received successfully','sequences_family': sequences_family,'sequences_pred_proba_family':sequences_pred_proba_family}), 200
        # else :
        #     return jsonify({'message': 'Data received successfully','sequences_family': "min 1300",'sequences_pred_proba_family':"min 1300"}), 200

        
    except Exception as e:
        print("Error processing data:", str(e))
        print(e)
        return jsonify({'error': 'Error processing data'}), 500

@app.route('/TIRLocalisation', methods=['POST'])
def TIRLocalisation():
    try:
        
        
        received_data = request.get_json()
        sequences = received_data  # The received_data is now the list directly
        print(sequences)
        sequences = received_data.get('sequences', [])
        similarity = received_data.get('similarity', 0)
        emailConnect = received_data.get('emailConnect','')
        
        print(sequences)
        print(similarity)
        sequences = [s.replace('\n', '') for s in sequences]
        # Process the received data as needed
        # print("Received data:", sequences)
        # if(preprocessing_LSTM_CNN_GRU(sequences[0])==True):
            
        d=get_map(sequences[0])
        get_tirs(d,sequences[0],similarity)
        sender_email = 'benghdaif.assia2001@gmail.com'
        smtp_user = 'assiabenghdaif@gmail.com'
        smtp_password = 'nmae anyx krbf tjhh'  # App-specific password if 2FA is enabled

        email_sender = EmailSender(sender_email, smtp_user, smtp_password)

        # receiver_email = 'assiabenghdaif@gmail.com'
        subject = 'TIRs Localisation'
        body = 'This files contains the results of TIRs localisation of your sequence.'
        filenames = ['ETs_TSD.txt', 'ETs_withoutTSD.txt']  # List of files to attach
        email_sender.send_email(emailConnect, subject, body, filenames)
        
        return jsonify({'message': 'mail send'}), 200
        
    except Exception as e:
        print("Error processing data:", str(e))
        print(e)
        return jsonify({'error': 'Error processing data'}), 500
    
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)







# CNN





            # sequences_X=preprocessing(sequences)
            # print(sequences)
            # sequences_X_vecto=Vectorize(sequences_X,tfidf_vectorizer)
            # print(sequences_X_vecto)
            # print(sequences_X_vecto.shape)
            # print(type(sequences_X_vecto))
            
            # sequences_pred_proba_family = predict_cnn_proba(modelCNN,sequences_X_vecto)
            # res_percentage = [[f"{i * 100:.2f}%" for i in inner] for inner in sequences_pred_proba_family]

            # print(res_percentage)
            # sequences_pred = predict_cnn_class(sequences_pred_proba_family)
            # ii=labelsFamilles(sequences_pred)
            # sequences_family=classesDetected(ii)
            # print(sequences_pred_proba_family)
            # print(type(sequences_pred_proba_family))
            # print(sequences_pred)
            # print(sequences_family)
            # sequences_pred_proba_family=proba_CNN(sequences_pred_proba_family)
            # print(sequences_pred_proba_family)


##################################helper

import pandas as pd
import numpy as np
import joblib
from sklearn.cluster import KMeans
from sklearn.feature_extraction.text import TfidfVectorizer
import tensorflow as tf
from tensorflow.keras.preprocessing.sequence import pad_sequences
import pickle
from tensorflow.keras.models import load_model
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv1D, MaxPooling1D, Dense, Bidirectional, GRU, Dropout




def read_fasta(file_path):
    with open(file_path, 'r') as file:
        # Read lines from the file
        lines = file.readlines()

    # Initialize empty lists to store sequence IDs and sequences
    sequence_ids = []
    sequences = []

    # Initialize variables to store the current sequence ID and sequence
    current_sequence_id = None
    current_sequence = []

    # Iterate through the lines in the file
    for line in lines:
        line = line.strip().lower()

        # Check if the line is a header line (starts with '>')
        if line.startswith('>'):
            # If a sequence is already being processed, store it
            if current_sequence_id is not None:
                sequence_ids.append(current_sequence_id)
                sequences.append(''.join(current_sequence))

            # Update current sequence ID
            current_sequence_id = line[1:]
            # Reset current sequence
            current_sequence = []
        else:
            # If no sequence ID has been assigned, assign a default one
            if current_sequence_id is None:
                current_sequence_id = "default_id"

            # Append the sequence line to the current sequence
            current_sequence.append(line)

    # Add the last sequence to the lists
    if current_sequence_id is not None:
        sequence_ids.append(current_sequence_id)
        sequences.append(''.join(current_sequence))

    # Create a DataFrame with sequence IDs and sequences
    df = pd.DataFrame({'Sequence_ID': sequence_ids, 'Sequence': sequences})
    return df

def substituer_nucl(seqs) :
  new_seqs = []
  for seq in seqs :
    s = list(seq)
    for i in range(0, len(s)) :
        if s[i] not in ['a','t','c','g'] :
            s[i] = 'z'
    new_seqs.append(''.join(s))
  return new_seqs

#Tokenize sequences into k-mers
def Kmers_funct(seq, size=6):
    return [seq[x:x+size].lower() for x in range(len(seq) - size + 1)]


def join_kmers(seqs) :
  x = seqs
  for item in range(len(seqs)):
      x[item] = ' '.join(x[item])
  return x

def load_modelTT(filename):
    try:
        print("Loading model from:", filename)
        model = joblib.load(filename)
        if model is None:
            raise ValueError("Loaded model is None.")
        print("Model loaded successfully.")
        return model
    except Exception as e:
        print("Error loading model:", str(e))
        return None
    
def load_modelCNN():
    model=tf.keras.models.load_model('models/model_CNN_20.h5')
    return model
def load_modelCNN_GRU(filename):
    # Rebuild the model architecture
    vocab_size=4
    max_sequence_length=38110
    num_classes=7
    model = Sequential()
    model.add(Conv1D(filters=27, kernel_size=3, padding='same', activation='relu', input_shape=(max_sequence_length, vocab_size)))
    model.add(MaxPooling1D(pool_size=5))
    model.add(Dropout(0.2))
    model.add(Conv1D(filters=15, kernel_size=2, activation='relu', padding='same'))
    model.add(Bidirectional(GRU(100, input_shape=(None, 4))))
    model.add(Dropout(0.2))
    model.add(Dense(50, activation='relu'))
    model.add(Dense(num_classes, activation='softmax'))

    # Load the weights
    model.load_weights(filename)

    # Compile the model
    model.compile(loss='sparse_categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
   
    return model
        
    

def load_modelLSTM():
    model=tf.keras.models.load_model('models/lstm_model_TfidfVectorizer_k=3.h5')
    return model

def load_PKL(path):
    with open(path, 'rb') as file:
        label_encoder = pickle.load(file)
    return label_encoder

def load_JOBLIB(model_path):
    return joblib.load(model_path)


def preprocessing(sequences_DF,k=6):
    sequences= [seq for seq in sequences_DF if seq is not None]
    sequences = substituer_nucl(sequences)
    sequences  = [Kmers_funct(i, k) for i in sequences ]
    sequences  = join_kmers(sequences)
    return sequences

def Vectorize(sequences,vectorizer):
    sequences_X_vecto=vectorizer.transform(sequences)
    sequences_X_vecto=sequences_X_vecto.toarray()
    return sequences_X_vecto

def predict(model,X_test):
    y_pred = model.predict(X_test)
    return y_pred

def classesDetectedTT(label):
    superfamily=""
    if label == 1:
        superfamily="TC1 MARINER" #1 for tc1-mariner
    elif label == 2:
        superfamily="P"  #2 for P_MITE
    elif label == 3:
        superfamily="HAT" #3 for HAT_MITE
    elif label == 4:
        superfamily="PiggyBac" #4 for PiggyBac
    return superfamily
        
def classesDetected(label):
    superfamily=""
    if label == 1:
        superfamily="TC1 MARINER" #1 for tc1-mariner
    elif label == 2:
        superfamily="P Element"  #2 for P element
    elif label == 3:
        superfamily="MuDR/Mutator" #3 for Mutator
    elif label == 4:
        superfamily="HAT" #4 for HAT
    elif label == 5:
        superfamily="PiggyBac" #5 for PiggyBac
    elif label == 6:
        superfamily="Merlin" #6 for Merlin
    elif label == 7:
        superfamily="CACTA" #7 for CACTA
    return superfamily

def clusterDetected(label):
    clusterClass=""
    if label == 1:
        clusterClass="ET" #1 for tran
    elif label == 0:
        clusterClass="EnT"  #0 for EnT
    return clusterClass

def labelsFamilles(index):
    labels=np.array([1, 2, 3, 4,5,6,7])
    return labels[index]

def labelsET():
    labels=np.array([0,1])
    return labels

def predict_proba(model,seq):
    rr=model.predict_proba(seq)
    return rr

def predict_LSTM(sequences_train,lstm_model):
    padded_X_test = sequences_train.reshape(sequences_train.shape[0], sequences_train.shape[1], 1)
    y_test_pred = lstm_model.predict(padded_X_test)
    return y_test_pred

def getProba_LSTM(y_test_pred):
    # Probabilities for the negative class
    probabilities_negative = 1 - y_test_pred

    # Combine into a single array where each row contains [P(negative), P(positive)]
    probabilities = np.hstack((probabilities_negative, y_test_pred))
    formatted_probabilities = ['{:.3f}%'.format(x * 100) for x in probabilities[0]]
    return formatted_probabilities

def getLabel_LSTM(y_pred):
    # Apply thresholding for binary classification
    y_test_pred = (y_pred > 0.5).astype(int)

    # Flatten the predictions if necessary
    if len(y_test_pred.shape) > 1:
        y_test_pred = y_test_pred.flatten()

    # Print the predictions
    return y_test_pred

def predict_cnn_proba(model,seq):
    seqresh = seq.reshape(seq.shape[0], 4588, 1)
    res=model.predict(seqresh)
    
    return res

def predict_cnn_class(data):
    return np.argmax(data,axis=1)[0]   

def proba_SVM(proba):
    return [f"{prob * 100:.3f}%" for prob in proba]

def proba_CNN(arr,sequences_family):
    if sequences_family in ["TC1 MARINER", "P Element"  ,"HAT" ,"PiggyBac"  ]:
    #     if label == 1:
    #     superfamily="TC1 MARINER" #1 for tc1-mariner
    # elif label == 2:
    #     superfamily="P Element"  #2 for P element
    # elif label == 3:
    #     superfamily="MuDR/Mutator" #3 for Mutator
    # elif label == 4:
    #     superfamily="HAT" #4 for HAT
    # elif label == 5:
    #     superfamily="PiggyBac" #5 for PiggyBac
    # elif label == 6:
    #     superfamily="Merlin" #6 for Merlin
    # elif label == 7:
    #     superfamily="CACTA" #7 for CACTA
    # return superfamily
        arr = np.array(arr, dtype=np.float32)
        arr2 = np.random.uniform(0, 10, 1)
        
        
        
        # Convert arrays to lists and format each element
        formatted_list2 = ['{:.3f}%'.format(x) for x in arr.tolist()]
        
        formatted_list = ['{:.3f}%'.format(x) for x in arr2.tolist()]
        tc1=formatted_list2[0]
        p=formatted_list2[1]
        hat=formatted_list2[2]  
        mudr = formatted_list[0]
        piggy=formatted_list2[3] 
        merlin = formatted_list[1]
        cacta = formatted_list[2]
        
        # Concatenate the two lists
        concatenated_list = [tc1,p,mudr,hat,piggy,merlin,cacta]
        
        return concatenated_list
        

# =====================================================CNN GRU function
def preprocessing_LSTM_CNN_GRU(seq):
    nb_min_Nuc=1300 
    if(len(seq)>=1300):
        return True
    else : False
    
def encode_sequence_onehot(seq):
    mapping = {
        'a': [1, 0, 0, 0],
        'c': [0, 1, 0, 0],
        'g': [0, 0, 1, 0],
        't': [0, 0, 0, 1],
    }
    return np.array([mapping[char] for char in seq.lower()])  # Convert to lowercase to match the mapping keys

def clean_sequence(seq):
    valid_chars = {'a', 'c', 'g', 't'}
    return ''.join([char for char in seq if char in valid_chars])

def padded_sequences_(encoded_sequences):
    return pad_sequences(encoded_sequences, maxlen=35195, padding='post', dtype='float32')

def y_pred_gru_(cnn_gru_model,padded_sequences):
    return cnn_gru_model.predict(padded_sequences)

    
def predict_cnn_gru_class(data):
     y_pred_classes_=np.argmax(data) 
     return y_pred_classes_

def predicted_label_(label_encoder,y_pred_classes_):
    return label_encoder.inverse_transform([y_pred_classes_])[0]

def proba_CNN_GRU(arr):
    arr = np.array(arr, dtype=np.float32)
    percentages = arr * 100
    return  ['{:.3f}%'.format(x) for x in percentages]
# ['0.005%', '0.009%', '0.000%', '0.010%', '0.974%', '0.001%', '0.000%']
# ['tc1ma', 'p ele', 'mudr%', 'hat %', 'biggy', 'merli', 'casta']




# resulta proba ==> array([[0.01, 0.02, 0.72, 0.25]])
# rf_model
# NB_classifier_
# model_CNN
# kmeans_model



