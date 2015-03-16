%{
This script gets the size of test data, both test A (first half) and test B (second half)
The result is fixed for every feature type:
	perm#   testA  testB
	1   	4402   3464
	2   	4119   4248
	3   	3583   4164
	4   	3769   3689
	5   	4302   3776
	6   	4301   3427
	7   	4476   3651
	8   	4014   4335
	9   	4020   3824
	10  	4274   4276
%}
A = zeros(10,2);
load('RandFileList.mat');% Load RandFileList var
numFile = size(RandFileList,1);
numTest = numFile/20;
startA = numFile - 2*numTest + 1;
endA = numFile - numTest;
startB = numFile - numTest + 1;
endB = numFile;

for t=1:10

	for qid=startA:endA
		load(['./data/' RandFileList(qid, t).name]);%load F and L
		numFrame = size(F,2);
		A(t, 1) = A(t, 1) + numFrame;
	end

	for qid=startB:endB
		load(['./data/' RandFileList(qid, t).name]);%load F and L
		numFrame = size(F,2);
		A(t, 2) = A(t, 2) + numFrame;
	end

end

A
