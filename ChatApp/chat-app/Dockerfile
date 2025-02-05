# Use the official Node.js image to build the Vue app
FROM node:16

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install
RUN npm install axios
RUN npm install vuex@next


# Copy the Vue.js application source code
COPY . .

# Expose the default port for Vue development server (8080)
EXPOSE 8081

# Run the Vue.js development server
CMD ["npm", "run", "serve"]
