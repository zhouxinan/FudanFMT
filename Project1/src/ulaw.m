function [z]=ulaw(y,u)
%		u-law nonlinearity for nonuniform PCM
%		X=ULAW(Y,U).
%		Y=input vector.

% todo:
x = y / max(abs(y));
% Compute u-law value.
z = log(1 + u .* abs(x)) / log(1 + u) .* sign(x);
end