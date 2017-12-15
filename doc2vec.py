import gensim
from gensim.models.doc2vec import TaggedDocument, LabeledSentence, Doc2Vec
import sys
model = Doc2Vec.load("D:\Studies\Masters\Semester1\Information Retrieval\Project\Data\doc2vec.model")
tokens = sys.argv[1].split()
dv = model.infer_vector(tokens)
# print(model.docvecs.most_similar(positive=[dv]))
temp_result = model.docvecs.most_similar(positive=[dv])
# result[1][0]
# len(result)
result = ""
for i in range(len(temp_result)):
    result = result + temp_result[i][0].lstrip() + ":"
    
print(result) 