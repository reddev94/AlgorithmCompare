FROM node:12 as build
WORKDIR /algorithm-compare-fe
COPY package*.json .
RUN npm install -g @angular/cli@11.2.19
RUN npm install
COPY . .
RUN ng build --prod AlgorithmCompareFE

FROM nginx
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
COPY --from=build /algorithm-compare-fe/dist/AlgorithmCompareFE /usr/share/nginx/html
EXPOSE 80