% Set the output filename.
outputFilename = 'out.gif';
% Set the background color to white so as to avoid grey border.
set(gcf, 'color', 'white');
for n = 1:16
    % Read the images sequentially using n.
    I = imread(sprintf('Frame%d.bmp', n));
    % Show the image.
    imshow(I);
    % Capture the figure as a movie frame.
    frame = getframe(gcf);
    % Get the indexed image data and associated colormap from the single movie frame.
    im = frame2im(frame);
    % Convert RGB image to indexed image with 256 colors.
    [image, map] = rgb2ind(im, 256);
    if n == 1
        % Write the first frame's image to the output file. Set LoopCount to Inf so as to
        % let the gif image loop infinitely. WriteMode should be 'overwrite' so
        % that a new gif file can be correctly created. DelayTime is set to
        % 0.1
        imwrite(image, map, outputFilename, 'gif', 'DelayTime', 0.1, 'WriteMode', 'overwrite', 'LoopCount', Inf);
    else
        % Write the other frames' images to the output file. WriteMode should be 'append' so
        % that the other frames' images can be correctly written. DelayTime
        % is set to 0.1
        imwrite(image, map, outputFilename, 'gif', 'DelayTime', 0.1, 'WriteMode', 'append');
    end
end