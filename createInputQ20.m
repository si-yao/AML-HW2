load('RandFileList.mat');% Load RandFileList var
% create input for q+2-0 using 30% first data
numFile = size(RandFileList,1);
numTrain = 3*numFile/10;
numTest = numFile/20;
prefix = './q20/q20.';

for t=1:10

f = fopen([prefix int2str(t) '.in'], 'w');
for qid=1:numTrain
	load(['./data/' RandFileList(qid, t).name]);%load F and L
	numFeat = size(F,1);	
	numFrame = size(F,2);
	for i=1:numFrame
		if(i==numFrame)
		F(numFeat+1:2*numFeat, i) = zeros(numFeat,1);
		continue;		
		end
		F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
	end
	for i=1:numFrame
		F(:,i) = [F(:,i); quadratic(F(:,i))];
	end
	numFeat = size(F, 1);
	for i=1:numFrame
		if(i<=2)
			F(numFeat+1:numFeat+12, i) = zeros(12,1);
		else
			F(numFeat+1:numFeat+12, i) = F(1:12, i-2);
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
		continue;		
		end
		F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
	end
	for i=1:numFrame
		F(:,i) = [F(:,i); quadratic(F(:,i))];
	end
	numFeat = size(F, 1);
	for i=1:numFrame
		if(i<=2)
			F(numFeat+1:numFeat+12, i) = zeros(12,1);
		else
			F(numFeat+1:numFeat+12, i) = F(1:12, i-2);
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
		continue;		
		end
		F(numFeat+1:2*numFeat, i) = F(1:numFeat,i+1);
	end
	for i=1:numFrame
		F(:,i) = [F(:,i); quadratic(F(:,i))];
	end
	numFeat = size(F, 1);
	for i=1:numFrame
		if(i<=2)
			F(numFeat+1:numFeat+12, i) = zeros(12,1);
		else
			F(numFeat+1:numFeat+12, i) = F(1:12, i-2);
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
