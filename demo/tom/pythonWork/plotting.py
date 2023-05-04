from sklearn.manifold import TSNE
import matplotlib.pyplot as plt
import word2vec
import numpy as np
from keras.integration_test.models.text_classification import custom_standardization
from keras.layers import TextVectorization
from gensim.models import word2vec
from gensim.models import Word2Vec


def tsne_plot(model):

    labels = []
    tokens = []

    for word in model.wv.key_to_index:
        tokens.append(model.wv[word])
        labels.append(word)

    tsne_model = TSNE(perplexity=40, n_components=2, init='pca', n_iter=2500)
    x = np.array([tokens])
    x = x.reshape([8445,100])
    new_values = tsne_model.fit_transform(x)

    x = []
    y = []
    for value in new_values:
        x.append(value[0])
        y.append(value[1])

    plt.figure(figsize=(8, 8))
    for i in range(250,320):
        plt.scatter(x[i], y[i])
        plt.annotate(labels[i],
                 xy=(x[i], y[i]),
                 xytext=(5, 2),
                 textcoords='offset points',
                 ha='right',
                 va='bottom')
    plt.show()
    x = 2
    y = 3


model = Word2Vec.load("WatsonW2V")
tsne_plot(model)
