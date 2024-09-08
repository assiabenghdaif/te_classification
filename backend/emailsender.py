import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.application import MIMEApplication
from email import encoders

class EmailSender:
    def __init__(self, sender_email, smtp_user, smtp_password, smtp_server='smtp.gmail.com', smtp_port=587):
        self.sender_email = sender_email
        self.smtp_user = smtp_user
        self.smtp_password = smtp_password
        self.smtp_server = smtp_server
        self.smtp_port = smtp_port

    def send_email(self, receiver_email, subject, body, filenames=[]):
        # Create a multipart message and set headers
        message = MIMEMultipart()
        message['From'] = self.sender_email
        message['To'] = receiver_email
        message['Subject'] = subject

        # Attach the body with the msg instance
        message.attach(MIMEText(body, 'plain'))

        # Attach the files
        for file in filenames:
            with open(file, 'rb') as attachment:
                # Add file as application/octet-stream
                part = MIMEApplication(attachment.read(), Name=file)

            # After the file is closed
            part['Content-Disposition'] = f'attachment; filename="{file}"'
            message.attach(part)

        try:
            # Connect to the server and send email
            server = smtplib.SMTP(self.smtp_server, self.smtp_port)
            server.starttls()  # Upgrade the connection to secure
            server.login(self.smtp_user, self.smtp_password)
            text = message.as_string()
            server.sendmail(self.sender_email, receiver_email, text)
            print('Email sent successfully')
        except Exception as e:
            print(f'Error: {e}')
        finally:
            server.quit()

# # Usage Example
# if __name__ == "__main__":
#     sender_email = 'benghdaif.assia2001@gmail.com'
#     smtp_user = 'assiabenghdaif@gmail.com'
#     smtp_password = 'nmae anyx krbf tjhh'  # App-specific password if 2FA is enabled

#     email_sender = EmailSender(sender_email, smtp_user, smtp_password)

#     receiver_email = 'assiabenghdaif@gmail.com'
#     subject = 'Subject of the Email'
#     body = 'This is the body of the email.'
#     filenames = ['ETs_TSD.txt', 'ETs_withoutTSD.txt']  # List of files to attach

#     email_sender.send_email(receiver_email, subject, body, filenames)
