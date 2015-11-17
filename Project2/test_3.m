% Read the image file.
filename = 'sky.jpg';
image = imread(filename);
% Show the original image in a window.
figure, imshow(image);
% Compute the threshold automatically.
thresh = graythresh(image);
% Convert image to binary image, based on threshold.
newImage = im2bw(image, thresh);
% Write the new image to file.
imwrite(newImage, 'sky_binary.jpg');
fprintf('The binary image has been saved to sky_binary.jpg.\n');
% Show the new image in a window.
figure, imshow(newImage);