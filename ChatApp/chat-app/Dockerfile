# Use node as the base image
FROM node:16

# Set the working directory inside the container
WORKDIR /app

# Install project dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy all files into the container
COPY . .

# Expose the port that Vue.js uses by default
EXPOSE 3000

# Run the Vue.js app
CMD ["npm", "run", "serve"]
