% Read the sample image file.
image = imread('pout.tif');
% Show the sample image.
figure, imshow(image);
% Show the histogram of image data.
figure, imhist(image);

% Grey color expansion.
J = imadjust(image, [0.2, 0.6], [0, 1]);
% Show the improved image.
figure, imshow(J);
% Show the histogram of the improved image.
figure, imhist(J);

% Enhance contrast using histogram equalization.
K = histeq(image);
% Show the improved image.
figure, imshow(K);
% Show the histogram of the improved image.
figure, imhist(K);

% Write the new images to files
imwrite(J, 'pout_imadjust.jpg');
imwrite(K, 'pout_histeq.jpg');
fprintf('The grey color expansion image has been saved to pout_imadjust.jpg.\n');
fprintf('The histogram equalization image has been saved to pout_histeq.jpg.\n');