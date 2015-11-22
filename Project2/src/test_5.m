% Read the sample image file.
image = imread('eight.tif');
% Add salt & pepper noise. The noise density is 0.04.
noiseImage = imnoise(image, 'salt & pepper', 0.04);
% Show the noise image.
figure, imshow(noiseImage);
% 2-D median filtering, window size 3 by 3.
filteredImage1 = medfilt2(noiseImage, [3 3]);
% Show filteredImage1.
figure, imshow(filteredImage1);
% Create averaging filter, window size 3 by 3.
h = fspecial('average', [3 3]);
% Use averaging filter to filter the noise image.
filteredImage2 = imfilter(noiseImage, h);
% Show filteredImage2.
figure, imshow(filteredImage2);
% Write the new images to files
imwrite(filteredImage1, 'eight_median_filter.jpg');
imwrite(filteredImage2, 'eight_averaging_filter.jpg');
fprintf('The 2-D median filtered image has been saved to eight_median_filter.jpg.\n');
fprintf('The averaging filtered image has been saved to eight_averaging_filter.jpg.\n');