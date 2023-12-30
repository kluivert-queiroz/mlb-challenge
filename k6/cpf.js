const randomDigit = () => {
    return Math.floor(Math.random() * 9)
}

const modByEleven = (dividend) => {
    return Math.round((dividend - (Math.floor(dividend / 11) * 11)))
}

export const generateValidCPF = () => {
    const n1 = randomDigit()
    const n2 = randomDigit()
    const n3 = randomDigit()
    const n4 = randomDigit()
    const n5 = randomDigit()
    const n6 = randomDigit()
    const n7 = randomDigit()
    const n8 = randomDigit()
    const n9 = randomDigit()
    let d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10

    d1 = 11 - (modByEleven(d1))

    if (d1 >= 10) d1 = 0

    let d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11

    d2 = 11 - (modByEleven(d2))

    if (d2 >= 10) d2 = 0
    return "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + d1 + d2
}
