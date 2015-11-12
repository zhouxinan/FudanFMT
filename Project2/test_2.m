% Read the image file.
filename = 'sky.jpg';
image = imread(filename);
% Translate the image matrix to a double type matrix to improve accuracy.
image = im2double(image);
% Take the red, green, blue matrices and compute the new image matrix
% using the formula in the PPT.
newImage = 0.29900 * image(:,:,1) + 0.58700 * image(:,:,2) + 0.11400 * image(:,:,3);
% Get the new image matrix as a one-dimensional array and compute its
% variance using the var() function of MATLAB.
variance = var(newImage(:));
% Show the new image in a window.
imshow(newImage);
% Write the new image to file.
imwrite(newImage, 'sky_grey.jpg');
% Show the variance in the console.
fprintf('The variance of the grey image is: %f\n', variance);