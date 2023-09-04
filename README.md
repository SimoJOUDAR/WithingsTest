# WithingsTest2 app


## Preview Video
https://github.com/SimoJOUDAR/WithingsTest/assets/80898080/07585af3-f8b6-4aa4-830e-1754c4afee11

https://github.com/SimoJOUDAR/WithingsTest/assets/80898080/f9a2ef8e-246c-4f99-84a1-db977cb765b9

## Intro
This is an Android app for generating gif images.

It uses pixabay api to search for images using keywords and displays them on a grid.
The user has the option to select images from the grid and generate a gif image.

The app uses StateFlow for data transmission, RecyclerView for grid display and WorkManager for bitmap processing.

## Instructions
Before compiling the project, add a PIXABAY_API_KEY to your local.properties file as the following: 
PIXABAY_API_KEY = your_key_here

## Obfuscation
The source code has obfuscation and shrinkResources enabled. It uses an external obfuscation dictionary available at https://bit.ly/3uGrnSu