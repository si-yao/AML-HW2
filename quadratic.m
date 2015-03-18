%A should be vector N*1, and Q is also a vector with N(N+1)/2 D
function [Q] = quadratic(A)
len = size(A, 1);
M = A * A';
Q = M(:,1);

for i=2:len
	Q = [Q; M(i:end,i)];
end

end