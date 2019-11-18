FROM node:lts-alpine as build-step
WORKDIR /usr/src/app
COPY package.json /usr/src/app
COPY . .
RUN npm install --save-dev node-sass
EXPOSE 8886
CMD npm run start