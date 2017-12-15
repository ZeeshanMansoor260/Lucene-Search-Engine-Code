import logging
import gensim
from gensim.models.keyedvectors import KeyedVectors
import sys

logging.basicConfig(level=logging.DEBUG)
model = KeyedVectors.load_word2vec_format('D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\Data\\word2vec\\word2vec2\\word2vec-master\\vectors-phrase.bin', binary=True, encoding= "UTF-8" )
# model = gensim.models.Word2Vec.load("D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\Data\\word2vec\\word2vec2\\word2vec-master\\outputs\\text8-vector.bin")
# print(sys.argv)
# print(model.most_similar(positive = sys.argv[1], topn= 5))
resultArray = model.most_similar(positive = sys.argv[1], topn= 5)
result = ""
for i in range(len(resultArray)):
	if(i == 4):
		result = result + resultArray[i][0] + ""  
	else:
		result = result + resultArray[i][0] + " , "  	
    
print(result)