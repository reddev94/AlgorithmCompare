FROM node:12
WORKDIR /algorithm-compare-fe
COPY package*.json /algorithm-compare-fe/
RUN npm install -g @angular/cli@11.2.19
RUN npm install
COPY . /algorithm-compare-fe/
CMD ng serve --host 0.0.0.0 --port 4200