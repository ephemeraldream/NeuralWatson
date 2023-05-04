import word2vec

from keras.integration_test.models.text_classification import custom_standardization
from keras.layers import TextVectorization
from gensim.models import word2vec
from gensim.models import Word2Vec


def main():
    f = open("questions.txt")
    ls = []
    checker = 0
    for i in range(401):
        if checker == 0:
            checker += 1
            f.readline()
            continue
        if checker == 1:
            ls.append(f.readline())
            checker += 1
        if checker == 2:
            f.readline()
            checker += 1
            continue
        if checker == 3:
            f.readline()
            checker = 0
    final_ls = []
    ls = ls[0:100]
    for i in ls:
        line = i.split(" ")
        line[-1] = line[-1].replace("\n", '')
        final_ls.append(line)
    model = Word2Vec.load("WatsonW2V")
    list_with_synonyms = []
    for sentence in final_ls:
        string = ""
        for word in sentence:
            if word in model.wv.key_to_index:
                synonym = model.wv.most_similar(word)[0][0]
            else:
                synonym = word
            string += synonym
            string += " "
        list_with_synonyms.append(string)
    f = open("../W2VGeneratedSynonyms.txt", 'w').close()
    f = open("../W2VGeneratedSynonyms.txt", "a")
    for i in range(100):
        f.write(list_with_synonyms[i])
        f.write("\n")
    f.close()


            


    x = 2


main()



















