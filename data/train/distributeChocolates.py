def distributeChocolates(chocolates, students):
	maxChocolates = -1
	totalChocolates = len(chocolates)
	for i in range(totalChocolates):
		totalSum = 0
		for j in range(i, totalChocolates, +1):
			totalSum += chocolates[j]
			if totalSum % students == 0 and maxChocolates < totalSum:
				maxChocolates = totalSum
	print maxChocolates

if __name__ == '__main__':
	chocolates = map(int, raw_input().strip().split(' '))
	students = int(raw_input())
	distributeChocolates(chocolates, students)



	