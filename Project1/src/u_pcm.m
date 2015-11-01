function [a_quan]=u_pcm(a,n)
%U_PCM  	uniform PCM quantization of a sequence
%       	[A_QUAN]=U_PCM(A,N)
%       	a=input sequence.
%       	n=number of quantization levels (even).
%           a_quan=quantized output before encoding.

% todo:

delta = (max(a) - min(a)) / n; % Calculate delta using n.
m = min(a) + delta .* (0:(n - 1)); % m is the array of measurement values.
q = m + delta / 2; % q is the array of quantized values.
for i = 1 : length(a)
    for j = 1 : n
        if a(i) >= m(j)
            a_quan(i) = q(j); % Use q(j) as quantized output.
        end
    end
end
