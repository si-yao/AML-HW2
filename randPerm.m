% this script randomly generate 10 permutations for training&testing
FileList = dir('./data/*.mat');
len = size(FileList,1);
% RandFileList = zeros(len, 10);
for i=1:10
  randomIdx = randperm(len);
  RandFileList(:,i) = FileList(randomIdx);
end
save('RandFileList.mat','RandFileList');
