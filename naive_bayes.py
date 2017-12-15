import pickle
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
import sys
def load_classifier():
   f = open('D:\Program Files\Eclipse Workspace\LuceneWeb\\nb.pickle', 'rb')
   classifier = pickle.load(f)
   f.close()
   return classifier

clf = load_classifier()
count_vect = CountVectorizer()
count_vect.vocabulary_.get(u'algorithm')
docs_new = ['lensgesture augmenting mobile interactions with back of device finger gestures', 'Hekaton: SQL servers memory-optimized OLTP engine', 'supervised learning in sensor networks new approaches with routing reliability optimizations']
X_new_counts = count_vect.transform(docs_new)
X_new_tfidf = tfidf_transformer.transform(X_new_counts)

predicted = clf.predict(X_new_tfidf)
print(predicted)