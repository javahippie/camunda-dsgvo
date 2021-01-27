import React, {Component} from 'react'

class DateDisplay extends Component {

    constructor(props) {
        super(props)
        this.state = {date: new Date(props.date)}
    }

    render() {
        return <span>{this.state.date.toLocaleDateString()} {this.state.date.toLocaleTimeString()}</span>
    }
}

export default DateDisplay