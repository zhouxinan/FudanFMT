% Get image filename.
filename = input('image:', 's');
% Get pixel coordinate.
x = input('x:');
y = input('y:');
% Read the image file.
I = imread(filename);
% Get image file size.
[maxY, maxX, z] = size(I);

% Check the limit.
if x <= 1 || x >= maxX
    fprintf('x is off the limit.\n');
    return;
end
if y <= 1 || y >= maxY
    fprintf('y is off the limit.\n');
    return;
end

% Print the result.
fprintf('(%d,%d):(%d,%d,%d)\n', x - 1, y - 1, I(x - 1, y - 1, 1), I(x - 1, y - 1, 2), I(x - 1, y - 1, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x, y - 1, I(x, y - 1, 1), I(x, y - 1, 2), I(x, y - 1, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x + 1, y - 1, I(x + 1, y - 1, 1), I(x + 1, y - 1, 2), I(x + 1, y - 1, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x - 1, y, I(x - 1, y, 1), I(x - 1, y, 2), I(x - 1, y, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x, y, I(x, y, 1), I(x, y, 2), I(x, y, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x + 1, y, I(x + 1, y, 1), I(x + 1, y, 2), I(x + 1, y, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x - 1, y + 1, I(x - 1, y + 1, 1), I(x - 1, y + 1, 2), I(x - 1, y + 1, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x, y + 1, I(x, y + 1, 1), I(x, y + 1, 2), I(x, y + 1, 3));
fprintf('(%d,%d):(%d,%d,%d)\n', x + 1, y + 1, I(x + 1, y + 1, 1), I(x + 1, y + 1, 2), I(x + 1, y + 1, 3));