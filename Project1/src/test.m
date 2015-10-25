t=0:0.0001:2*pi;
y=cos(t);
z1=u_pcm(y,64);
z2=ula_pcm(y,64,255);
plot(t,y);
hold on
plot(t,z1,'r');
hold on
plot(t,z2,'g');