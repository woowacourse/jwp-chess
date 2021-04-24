export const parseDomFromString = string => document.createRange().createContextualFragment(string).firstElementChild
export const $ = selector => document.querySelector(selector)