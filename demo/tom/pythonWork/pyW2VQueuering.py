import numpy as np
import tensorflow as tf
import keras
from keras.integration_test.models.text_classification import custom_standardization
from keras.layers import TextVectorization
import collections
import word2vec

def main():
    # array_of_strings = []
    # list = []
    # f = open("dataset.txt")
    # allIn = f.read()
    # array_of_strings = allIn.split(",")
    # for i in range(len(array_of_strings)):
    #     if len(array_of_strings[i]) < 15:
    #         list.append(str(array_of_strings[i]))
    #     array_of_strings[i] = array_of_strings[i].replace(" ", "")
    # new_array = []
    # with open('no_long_words.txt', 'w') as f:
    #     for i in range(len(list)):
    #         f.write(f"{list[i]}\n")
    f = open("no_long_words.txt")
    topList = []
    allIn = f.read().split("\n")
    for i in range(len(allIn)):
        allIn[i] = allIn[i].replace(" ", '')
    count = 15
    string = ""
    final_list = []
    temp = []
    for i in range(len(allIn)):
        if count == 15:
            final_list.append(temp)
            count = 0
            temp = []
        else:
            temp.append(allIn[i])
            count +=1

    model = word2vec.Word2Vec(final_list, window = 5, min_count=1000, workers=4)
    model.save("WatsonW2V")
























main()


