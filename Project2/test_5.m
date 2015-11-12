% Read the sample image file.
image = imread('eight.tif');
% Add salt & pepper noise. The noise density is 0.04.
noiseImage = imnoise(image, 'salt & pepper', 0.04);
% Show the noise image.
figure, imshow(noiseImage);
% 2-D median filtering
filteredImage1 = medfilt2(noiseImage, [3 3]);
% Show filteredImage1.
figure, imshow(filteredImage1);
% Create averaging filter.
h = fspecial('average', [3 3]);
% Use averaging filter to filter the noise image.
filteredImage2 = imfilter(noiseImage, h);
% Show filteredImage2.
figure, imshow(filteredImage2);