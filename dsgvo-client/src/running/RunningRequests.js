import React, {Component} from 'react'
import {Panel, PanelGroup, Button} from 'rsuite'
import './Running.css'
import {withRouter} from 'react-router-dom'

class RunningRequests extends Component {

    constructor(props) {
        super(props)
        this.state = {instances: []}
    }

    componentDidMount() {
        fetch("/engine-rest/process-instance?processDefinitionKey=DSGVO_Process")
            .then(response => response.json())
            .then(result => this.setState({instances: result}))
    }

    render() {
        return <div className="processes-form">
            <h1>Laufende Anfragen</h1>
            <PanelGroup>
                {this.state.instances.map(instance =>
                    <Panel key={instance.id} header={<h2>{instance.businessKey}</h2>} bordered>
                        <Button appearance="primary" onClick={() => this.props.history.push(`/cancel/${instance.id}`)}>Stornieren</Button>
                    </Panel>)}
            </PanelGroup>
        </div>
    }

}

export default withRouter(RunningRequests)