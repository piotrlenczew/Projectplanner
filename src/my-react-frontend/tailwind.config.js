/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["./src/**/*.{js,jsx,ts,tsx}"],
    theme: {
        extend: {
            colors: {
                "custom-background": "#e6ffff",
                "custom-gray": "#60656f",
                "custom-blue": "#b7c9e2",
                "custom-blue-hov": "#95a9c0",
                "custom-myinput-bor": "#c9c9c9",
            },
        },
    },
    plugins: [],
}
