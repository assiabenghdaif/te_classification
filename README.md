# This project focuses on bioinformatics and AI for the classification and detection of transposable elements in DNA sequences, integrated into a mobile app for Android.
The project consists of a client-server architecture, where the client is a mobile application developed with Java for Android, and the server is a Flask application with a RESTful API (web services) using Python. The services include preprocessing functions (tokenization, vectorization) and already saved ML/DL models. The data is transformed with HTTP requests in JSON format.
![archi mob](https://github.com/user-attachments/assets/7bab7995-9e78-4dfc-9296-3f6b3cad9f86)

## 1. Project Objectives
 - Classifying DNA sequences into transposable and non-transposable elements.
 - Determining the type of transposable element among several specific classes.
 - Precisely locating the TIRs (Terminal Inverted Repeats) in the input sequence.

## 2. Data Sources
We worked with a DNA sequence database where 70% was collected by myself from the NCBI,DFAM, UniPROT websites, and 30% was provided by our biologist colleagues. The sequences were cleaned and normalized to ensure the quality of the input data.

## 3. Methodology
### Step 1: Binary Classification
 Model Used: Long Short Term Memory (LSTM) .
 Description: In this first step, DNA sequences are classified into two categories: transposable and non-transposable elements. The SVM model with a polynomial kernel was chosen due to its ability to handle high-dimensional data and capture non-linear relationships.
 Results: The obtained accuracy was 98.5%.

### Step 2: Multiclass Classification 
 Model Used: A hybrid CNN-GRU based model.
 Description: Sequences identified as containing transposable elements in the previous step are then classified into specific types. A CNN-GRU model was used for this task due to its ability to capture complex patterns in DNA sequences.
 Results: The obtained accuracy | presision | recall | f1-score was 90%|91%...
 Classes: TIR transposons: TC1 MARINER, HAT, PiggyBac, Mutator, Merlin, P element, CACTA (class 2).

### Step 3: TIR Localization
 Description: After the multiclass classification is completed, a Python script will be used to perform precise localization of TIRs in the transposable element sequences. This step aims to determine the exact positions of TIRs, providing crucial information for the biological analysis of DNA sequences. 
 Results: The results will be sent to the connected email.
