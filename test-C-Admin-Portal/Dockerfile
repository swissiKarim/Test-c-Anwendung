FROM node:lts-alpine as build-step
WORKDIR /app
COPY package.json ./
COPY . .
RUN npm install --save-dev node-sass
EXPOSE 4200
CMD npm run start