import pandas as pd
import numpy as np
import warnings
warnings.filterwarnings("ignore")
#data = pd.read_csv("diemthidhqg2020.csv")
data = pd.read_csv("D:/Teaching/VGU/IntroductionToDataScience/AIProject/diemthidhqg2020.csv")
print(data.head(5))
print(data.info())


toan = data ["toan_hoc"].dropna()
print(len(toan))

ngu_van = data ["ngu_van"].dropna()
print (len(ngu_van))# 72648

tieng_anh = data ["tieng_anh"].dropna()
print (len(tieng_anh)) #65898

#Step 4
import matplotlib.pyplot as plt

# plt.plot(toan.value_counts().sort_index(), color='red', linestyle = ':', marker='.', markersize=10)
# plt.grid(True)
# plt.legend(['Mon Toan'])
# plt.xlabel('Diem')
# plt.ylabel('So luong')
# plt.title('Thong ke diem mon Toan')
# plt.show()

#Step 5
plt.figure(figsize = (15,5))
diem_thi = ngu_van.value_counts().sort_index().index.tolist()
diem_thi = list(map(str,diem_thi))
plt.bar(diem_thi, ngu_van.value_counts().sort_index())
plt.show()

#Step 6
# van = round(ngu_van*2)/2
# plt.figure(figsize = (15,5))
# diem_thi = van.value_counts().sort_index().index.tolist()
# diem_thi = list(map(str,diem_thi))
# plt.bar(diem_thi, van.value_counts().sort_index())
# plt.grid(True)
# plt.bar(diem_thi, van.value_counts().sort_index(), color='g')
# plt.legend(['Mon Van'])
# plt.xlabel('Diem')
# plt.ylabel('So luong')
# plt.title('Thong ke diem mon Ngu Van')
# plt.show()

#Step 7

anh = round(tieng_anh*1)/1
diem_thi = ["Diem 0", "Diem 1", "Diem 2", "Diem 3", "Diem 4", "Diem 5", "Diem 6", "Diem 7", "Diem 8", "Diem 9", "Diem 10"]
explode = [0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 0]
plt.pie(anh.value_counts().sort_index(), pctdistance=1.2, autopct='%1.2f%%', wedgeprops={'edgecolor': 'white', 'linewidth':0.5}, explode=explode)
plt.figlegend(diem_thi)
plt.title('Thong ke diem mon Tieng Anh')
plt.show()


ten = data['ten'].dropna()
ten_string = ' '.join(ten)

from wordcloud import WordCloud
from matplotlib import rcParams
from PIL import Image

rcParams['figure.figsize'] = 10,15
mask = np.array(Image.open('vn_mask.jpg'))
wordcloud = WordCloud(max_words=len(ten), background_color="white", mask=mask, max_font_size=100, scale=8, collocations=False).generate(ten_string)

plt.imshow(wordcloud)
plt.axis('off')
plt.show()