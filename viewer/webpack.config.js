const path = require('path')

module.exports = {
    entry: './src/main/js/index.tsx',
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            },
            { enforce: 'pre', test: /\.js$/, loader: 'source-map-loader' },
            {
                test: /\.css$/,
                use: [
                    "style-loader",
                    "css-loader"
                ]
            }
        ]
    },
    resolve: {
        extensions: [ '.tsx', '.ts', '.js' ]
    },
    mode: 'development',
    devtool: 'inline-source-map',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist')
    }
    // mode: 'production',
    // devtool: 'source-map',
    // output: {
    //     filename: 'bundle.js',
    //     path: path.resolve(__dirname, 'dist')
    // }
};
