function [a_quan]=ula_pcm(a,n,u)
%ULA_PCM 	u-law PCM encoding of a sequence
%       	[A_QUAN]=MULA_PCM(X,N,U).
%       	X=input sequence.
%       	n=number of quantization levels (even).     	
%		a_quan=quantized output before encoding.
%       U the parameter of the u-law

% todo:
temp = ulaw(a,u); % U-law encoding.
temp = u_pcm(temp, n); % Uniform PCM encoding.
temp = inv_ulaw(temp, u); % Inverse U-law encoding.
a_quan = max(abs(a)) .* temp; % Get the final result.
end
