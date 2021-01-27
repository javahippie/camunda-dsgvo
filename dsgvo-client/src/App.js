import React, {Component} from 'react'
import './App.css'
import 'rsuite/dist/styles/rsuite.min.css'
import NavBar from "./navbar/NavBar"

class App extends Component {

    render() {
        return (
           <div>
               <NavBar/>
           </div>
        )
    }
}


export default App
