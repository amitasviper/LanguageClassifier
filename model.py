from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.naive_bayes import MultinomialNB

import os

target_names = ['C', 'Java', 'Python']
count_vectorizer = CountVectorizer()
tf_transformer = TfidfTransformer()
clf = None

def readFiles(extension, directory):
    files = os.listdir(os.path.join(os.getcwd(), 'data', directory))
    data = []
    for onefile in files:
        filename, ext = os.path.splitext(onefile)
        text = ""
        if ext.lower() == extension.lower():
            with open(os.path.join('data', directory, onefile), 'r') as f:
                text = f.read()
            data.append(text)
    return data

def prepareData(data, languageIndex):
    y = [languageIndex]*len(data)
    return y


def loadClassifier():
    global clf, target_names, count_vectorizer, tf_transformer

    c_data = readFiles('.c', 'train')
    java_data = readFiles('.java', 'train')
    python_data = readFiles('.py', 'train')

    x_data = []
    y_data = []

    x_data += c_data
    y_data += prepareData(c_data, target_names.index('C'))

    x_data += java_data
    y_data += prepareData(java_data, target_names.index('Java'))

    x_data += python_data
    y_data += prepareData(python_data, target_names.index('Python'))

    X_train_counts = count_vectorizer.fit_transform(x_data)

    X_train_tf = tf_transformer.fit_transform(X_train_counts)
    X_train_tf.shape

    clf = MultinomialNB().fit(X_train_tf, y_data)


def prefictLanguage(text):
    global count_vectorizer, tf_transformer, clf
    x_test_data = [text]

    X_new_counts = count_vectorizer.transform(x_test_data)
    X_new_tfidf = tf_transformer.transform(X_new_counts)

    predicted_category = clf.predict(X_new_tfidf)
    return target_names[predicted_category[0]]


