// if you run this in Chrome and are already signed
// into AOC, the session cookie shouldnt be needed
fetch('https://adventofcode.com/2023/day/3/input')
	.then(response => response.text().then(data =>
		alert("Answer for the first half of puzzle 3 is: " + solveFirstHalf(data) + ".\n" +
			  "Answer for the second half of puzzle 3 is: " + solveSecondHalf(data) + ".")
))

/**
 * @param {String} data The input for the puzzle, a 140x140 square of text.
 */
function solveFirstHalf(data) {
	console.log("Solving first half!")
	let result = 0
	let lineNum = 0
	let positions = Array.from(Array(140), () => Array(140)) // 2d array (y, x) (cuz thats easier)
	let lines = data.split('\n');
	lines.pop()
	lines.forEach((line, lineNum) => { // last line is blank
		let number = ''
		for (let charNum = 0; charNum < line.length; charNum++) {
			let c = line[charNum]
			if (/\d/.test(c))
				number += c
			else if (number) { // empty string is falsey
				positions[lineNum][charNum - number.length] = number
				number = ''
			} else if (/[-*+=@#$%&/\d]/.test(c))
				positions[lineNum][charNum] = c
		}
	})
	console.log("Finished mapping all numbers and symbols.")
	for (let y = 0; y < positions.length; y++) { // using this kind of loop so i can skip over unneeded stuff
		let column = positions[y]
		for (let x = 0; x < column.length; x++) { // same here
			let number = column[y]
			if (!/^\d+$/.test(number))
				continue
			let endX = x + number.length > 137 ? 139 : x + number.length + 1 // basically x + number.length + 1 but it wont pass 139 (for the array)
			let endY = y > 137 ? 139 : y + 1 // basically y + 1 but it wont pass 139 (for the array)
			searchY:
			for (let searchY = y < 2 ? 0 : y - 1; searchY < endY; searchY++) {
				for (let searchX = x < 2 ? 0 : x - 1; // x - 1 but it wont go under 0
					 searchX < endX;
					 searchY === y && searchX == x ?
						 searchX += number.length :
						 searchX++
				) {
					let symbol = positions[searchY][searchX]
					if (symbol) {
						result += parseInt(number)
						break searchY
					}
				}
			}
			x += number.length
		}
	}
	console.log("Finished solving first half!")
	return result
}

/**
 * @param {String} data The input for the puzzle, a 140x140 square of text.
 */
function solveSecondHalf(data) {
	console.log("Solving second half!")
	console.log("Finished solving second half!")
	return
}