from flask import Flask, request, jsonify
from helper import *
from fonctionsTIR import *
from emailsender import *
from tensorflow.keras.models import load_model



app = Flask(__name__)

label_encoder = load_PKL("models/label_encoder_for_dna_ML_CNN_GRU_onehot.pkl")
cnn_gru_model=load_modelCNN_GRU("models/model_weights_dna_ML_CNN_GRU_onehot_v3.weights.h5")
lstm_model = load_modelLSTM()
vectorizer_lstm = load_JOBLIB("models/TfidfVectorizer_vectorizer_lstm_k=3.joblib")
k=3

@app.route('/send_data', methods=['POST'])
def receive_data():
    try:
        
       
        # Assuming the data is sent as JSON
        received_data = request.get_json()
        sequences = received_data  # The received_data is now the list directly
        sequences = [s.replace('\n', '') for s in sequences]
        sequences = [s.lower() for s in sequences]
        # Process the received data as needed
        # print("Received data:", sequences)
       
            

            # for LSTM
        sequences_X=preprocessing(sequences,k)
        print(sequences)
        sequences_X_vecto=Vectorize(sequences_X,vectorizer_lstm)
        print(sequences_X_vecto)
        print(sequences_X_vecto.shape)
        print(type(sequences_X_vecto))
        sequences_pred = predict_LSTM(sequences_X_vecto,lstm_model)
        sequences_pred_proba = getProba_LSTM(sequences_pred)
        sequences_label=getLabel_LSTM(sequences_pred)
        # sequences_classe=[]
        # for seq in sequences_pred:
        sequences_classe=clusterDetected(sequences_label[0])
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
        
        
        # Assuming the data is sent as JSON
        received_data = request.get_json()
        sequences = received_data  # The received_data is now the list directly
        
        sequences = received_data.get('sequences', [])
        
        
        print(sequences)
        sequences = [s.replace('\n', '') for s in sequences]
        

        cleaned_sequences = [clean_sequence(seq) for seq in sequences]
        # print(cleaned_sequences)
        encoded_sequences = [encode_sequence_onehot(seq) for seq in cleaned_sequences]
        # print(encoded_sequences)
        padded_sequences = padded_sequences_(encoded_sequences)
        y_pred_gru = y_pred_gru_(cnn_gru_model,padded_sequences)
        print("y_pred_gru",y_pred_gru)
        res_percentage_cnn_gru=proba_CNN_GRU(y_pred_gru[0])
        # res_percentage_cnn_gru=['0.005%', '0.009%', '0.000%', '0.010%', '0.974%', '0.001%', '0.000%']
        y_pred_classes_=predict_cnn_gru_class(y_pred_gru[0])
        predicted_label = predicted_label_(label_encoder,y_pred_classes_)
        classesDetected_name=classesDetected(predicted_label)
        # classesDetected_name="PiggyBac"

            
            
        return jsonify({'message': 'Data received successfully','sequences_family': classesDetected_name,'sequences_pred_proba_family':res_percentage_cnn_gru}), 200
        
        
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
            # print(sequences_pred_proba_family)
