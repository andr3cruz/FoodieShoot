import os
import tarfile
import requests 
import collections
from collections import defaultdict
from shutil import copy, copytree, rmtree

def download_url(url, save_path, chunk_size=128):
    r = requests.get(url, stream=True)
    with open(save_path, 'wb') as fd:
        for chunk in r.iter_content(chunk_size=chunk_size):
            fd.write(chunk)

class DataProcessor():
    def __init__(self):
        return
    
    def __downloadUrl(self,url,save_path,chunck_size=128):
        req = requests.get(url,stream=True)
        with open(save_path,'wb') as fd:
            for chunk in req.iter_content(chunck_size=chunck_size):
                fd.write(chunk)

    def __unzipFile(self,file):
        if file.endswith('tar.gz'):
            tar = tarfile.open(file,'r:gz')
        elif file.endswith('tar'):
            tar = tarfile.open(file,'r:')
        tar.extractall()
        tar.close()

    def dataExtraction(self,url=None):
        if url is None:
            url = 'http://data.vision.ee.ethz.ch/cvl/food-101.tar.gz'
        path = 'food-101'
        if path in os.listdir():
            print('Dataset already exists')
            return
        print('Downloading data ...')
        self.__downloadUrl(url,path)
        print('Data downloaded ...\nBeggining extraction ...')
        file = 'food-101.tar.gz'
        self.__unzipFile(file)
        print('Extraction complete')
        print('Removing tar file')
        os.remove(file)
    
    def dataPrepare(self,filepath,src,dest):
        #split data into train and test dataset
        if os.path.exists(dest):
            print('Path already exists.\nDo you want to continue y/n (all data will be )')
            inp = str(input())
            if inp == 'y':
                print('Replacing data ...')
                rmtree(dest)
            else:
                print('Operation canceled')
                return
            classes_images = defaultdict(list)
            with open(filepath,'r') as txt:
                paths = [line.strip() for line in txt.readlines()]
                for p in paths:
                    food = p.split('/')
                    classes_images[food[0]].append(food[1] + '.jpg')
            for food in classes_images.keys():
                print('Copying images into ',food)
                if not os.path.exists(os.path.join(dest,food)):
                    os.makedirs(os.path.join(dest,food))
                for i in classes_images[food]:
                    copy(os.path.join(src,food,i), os.path.join(dest,food,i))
            print('All done')

    def dataSorted(self,data_dir='food-101/images/'):
        return sorted(os.listdir(data_dir))



