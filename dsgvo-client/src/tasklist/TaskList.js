import React, {Component} from 'react'
import './TaskList.css'
import {Panel, PanelGroup, Button} from 'rsuite'
import DateDisplay from "../util/DateDisplay"
import {withRouter} from 'react-router-dom'

class TaskList extends Component {

    constructor(props) {
        super(props)
        this.state = {tasks: []}
        this.handleChooseTask = this.handleChooseTask.bind(this);
    }

    componentDidMount() {
        fetch('/engine-rest/task?processDefinitionKey=DSGVO_Process&sortBy=created&sortOrder=desc')
            .then(response => response.json())
            .then(data => this.setState({tasks: data}))
    }

    handleChooseTask(id, taskDefinitionKey) {
        this.props.history.push(`/task/${taskDefinitionKey}/${id}`)
    }

    render() {
        return (
            <div className="task-form">
                <h1>Meine Aufgaben</h1>
                <PanelGroup className="taskList">
                    {this.state.tasks.map(data =>
                        <Panel header={<h2>{data.name}</h2>} key={data.id}>
                            Erstellt: <DateDisplay date={data.created}/>
                            <Button appearance="primary"
                                    onClick={() => this.handleChooseTask(data.id, data.taskDefinitionKey)}>Task
                                bearbeiten</Button>
                        </Panel>)}
                </PanelGroup>
            </div>
        )
    }
}

export default withRouter(TaskList)
