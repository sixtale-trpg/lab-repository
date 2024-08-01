# Step 1: Build the Vue.js application using Node.js
FROM node:16-alpine as build-stage

# Set the working directory inside the container
WORKDIR .

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the rest of the application code to the container
# COPY . .

# Build the Vue.js application
RUN npm run build

# Step 2: Use the Nginx image to serve the app
FROM nginx:stable-alpine

RUN ls -al

# Copy the build output from the previous stage to Nginx's HTML directory
COPY dist /usr/share/nginx/html

# Copy the Nginx configuration file
COPY nginx/nginx.conf /etc/nginx/nginx.conf

# Expose ports for HTTP and HTTPS
EXPOSE 80
EXPOSE 443

# Start Nginx when the container starts
CMD ["nginx", "-g", "daemon off;"]
