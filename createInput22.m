load('RandFileList.mat');% Load RandFileList var
% create input for +2-2 using 30% first data
numFile = size(RandFileList,1);
numTrain = 3*numFile/10;
numTest = numFile/20;
prefix = './22/22.';

for t=1:10

f = fopen([prefix int2str(t) '.in'], 'w');
for qid=1:numTrain
	load(['./data/' RandFileList(qid, t).name]);%load F and L
	numFeat = size(F,1);	
	numFrame = size(F,2);
	for i=1:numFrame
		if(i==numFrame)
			F(numFeat+1:2*numFeat, i) = zeros(numFeat,1);
			F(2*numFeat+1:3*numFeat) = zeros(numFeat,1);
		elseif(i==numFrame-1)
			F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
			F(2*numFeat+1:3*numFeat) = zeros(numFeat,1);
		else
			F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
			F(2*numFeat+1:3*numFeat) = F(1:numFeat, i+2);
		end
		if(i==1)
			F(3*numFeat+1:4*numFeat, i) = zeros(numFeat, 1);
			F(4*numFeat+1:5*numFeat, i) = zeros(numFeat, 1);
		elseif(i==2)
			F(3*numFeat+1:4*numFeat, i) = F(1:numFeat, i-1);
			F(4*numFeat+1:5*numFeat, i) = zeros(numFeat, 1);
		else
			F(3*numFeat+1:4*numFeat, i) = F(1:numFeat, i-1);
			F(4*numFeat+1:5*numFeat, i) = F(1:numFeat, i-2);
		end
	end
	numFeat = size(F, 1);
	
	for frame=1:numFrame
		label = L(frame) + 1;
		fprintf(f, '%d qid:%d', label, qid);
		for feat=1:numFeat
			fprintf(f, ' %d:%f', feat, F(feat, frame));
		end
		fprintf(f, '\n');
	end
end
fclose(f);

f = fopen([prefix int2str(t) '.a.test'], 'w');
startTest = numFile - 2*numTest + 1;
endTest = numFile - numTest;
q = 1;
for qid=startTest:endTest
	load(['./data/' RandFileList(qid, t).name]);
	numFrame = size(F,2);
	numFeat = size(F,1);
	for i=1:numFrame
		if(i==numFrame)
			F(numFeat+1:2*numFeat, i) = zeros(numFeat,1);
			F(2*numFeat+1:3*numFeat) = zeros(numFeat,1);
		elseif(i==numFrame-1)
			F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
			F(2*numFeat+1:3*numFeat) = zeros(numFeat,1);
		else
			F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
			F(2*numFeat+1:3*numFeat) = F(1:numFeat, i+2);
		end
		if(i==1)
			F(3*numFeat+1:4*numFeat, i) = zeros(numFeat, 1);
			F(4*numFeat+1:5*numFeat, i) = zeros(numFeat, 1);
		elseif(i==2)
			F(3*numFeat+1:4*numFeat, i) = F(1:numFeat, i-1);
			F(4*numFeat+1:5*numFeat, i) = zeros(numFeat, 1);
		else
			F(3*numFeat+1:4*numFeat, i) = F(1:numFeat, i-1);
			F(4*numFeat+1:5*numFeat, i) = F(1:numFeat, i-2);
		end
	end
	numFeat = size(F, 1);

	for frame=1:numFrame
		label = L(frame) + 1;
		fprintf(f, '%d qid:%d', label, q);
		for feat=1:numFeat
			fprintf(f, ' %d:%f', feat, F(feat, frame));
		end
		fprintf(f, '\n');
	end
	q = q+1;
end
fclose(f);

f = fopen([prefix int2str(t) '.b.test'], 'w');
startTest = numFile - numTest + 1;
endTest = numFile;
q = 1;
for qid=startTest:endTest
	load(['./data/' RandFileList(qid, t).name]);
	numFrame = size(F,2);
	numFeat = size(F,1);
	for i=1:numFrame
		if(i==numFrame)
			F(numFeat+1:2*numFeat, i) = zeros(numFeat,1);
			F(2*numFeat+1:3*numFeat) = zeros(numFeat,1);
		elseif(i==numFrame-1)
			F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
			F(2*numFeat+1:3*numFeat) = zeros(numFeat,1);
		else
			F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
			F(2*numFeat+1:3*numFeat) = F(1:numFeat, i+2);
		end
		if(i==1)
			F(3*numFeat+1:4*numFeat, i) = zeros(numFeat, 1);
			F(4*numFeat+1:5*numFeat, i) = zeros(numFeat, 1);
		elseif(i==2)
			F(3*numFeat+1:4*numFeat, i) = F(1:numFeat, i-1);
			F(4*numFeat+1:5*numFeat, i) = zeros(numFeat, 1);
		else
			F(3*numFeat+1:4*numFeat, i) = F(1:numFeat, i-1);
			F(4*numFeat+1:5*numFeat, i) = F(1:numFeat, i-2);
		end
	end
	numFeat = size(F, 1);

	for frame=1:numFrame
		label = L(frame) + 1;
		fprintf(f, '%d qid:%d', label, q);
		for feat=1:numFeat
			fprintf(f, ' %d:%f', feat, F(feat, frame));
		end
		fprintf(f, '\n');
	end
	q = q+1;
end
fclose(f);


end
