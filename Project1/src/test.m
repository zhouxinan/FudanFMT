t = 0:0.0001:2*pi;
y = cos(t);
z1 = u_pcm(y, 64);
z2 = ula_pcm(y, 64, 255);
% Draw the original signal.
plot(t, y);
hold on
% Draw the uniform-PCM-encoded signal.
plot(t, z1, 'r');
hold on
% Draw the u-law-encoded signal.
plot(t, z2, 'g');