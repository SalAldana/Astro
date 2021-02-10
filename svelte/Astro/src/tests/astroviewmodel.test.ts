import dayjs from 'dayjs'
import { dateIsInValidRange, getIntValue } from '../models/astroviewmodel'

describe("Test if Date is allowed", () => {
    test("Date is Allowed", () => {
        let today = new Date()
        expect(dateIsInValidRange(today)).toEqual(true)
    })
    test("Future Date isn't Allowed", () => {
        let futureDate = dayjs().add(10, 'year').toDate()
        expect(dateIsInValidRange(futureDate)).toEqual(false)
    })
    test("Future Date is allowed", () => {
        let futureDate = dayjs().add(3, 'year').toDate()
        expect(dateIsInValidRange(futureDate)).toEqual(true)
    })
    test("Past Date isn't Allowed", () => {
        let pastDate = dayjs().subtract(10, 'year').toDate()
        expect(dateIsInValidRange(pastDate)).toEqual(false)
    })
    test("Past Date is Allowed", () => {
        let pastDate = dayjs().subtract(3, 'year').toDate()
        expect(dateIsInValidRange(pastDate)).toEqual(true)
    })
})

describe("Test Number converter", () => {
    test("Convert Negative decimal", () => {
        expect(getIntValue(-2.6)).toEqual(-2)
    })
    test("Convert postive number", () => {
        expect(getIntValue(2.6)).toEqual(3)
    })
})