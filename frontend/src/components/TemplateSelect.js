import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Dropdown} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {fetchTemplates, setTemplate} from '../actions/contract-templates';

class TemplateSelectView extends Component {

  state = {
    activeTemplate: null
  };

  componentDidMount() {
    this.props.actions.fetchTemplates();
  }

  handleOnSelectTemplate = (e, {value}) => {
    const {scaffoldTemplates} = this.props;
    const activeTemplate = scaffoldTemplates.find(it => it.name === value);
    if (!activeTemplate) {
      return;
    }
    const {id, name, conversionAmount, ...formValues} = activeTemplate;
    this.setState({activeTemplate: name});
    this.props.actions.setTemplate(formValues);
  };

  render() {
    const {scaffoldTemplates} = this.props;
    const templateOptions = scaffoldTemplates.map(template => ({
      key: template.name,
      value: template.name,
      text: template.name
    }));

    if (!templateOptions) {
      return null;
    }

    return (
      <Dropdown fluid search
                selection
                placeholder="Select Scaffold Template"
                value={this.state.activeTemplate}
                onChange={this.handleOnSelectTemplate}
                options={templateOptions}/>
    );
  }
}

const mapStateToProps = ({scaffoldTemplates}) => ({scaffoldTemplates});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({setTemplate, fetchTemplates}, dispatch),
});

export const TemplateSelect = connect(mapStateToProps, mapDispatchToProps)(TemplateSelectView);
