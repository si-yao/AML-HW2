% this script convert a structured mat to plain mat
load('RandFileList.mat');
n =  size(RandFileList, 1);
for t=1:10
	for i=1:n
		FileMat{i, t} = RandFileList(i, t).name;
	end
end

save('-ascii', 'FileMat.mat', 'FileMat');