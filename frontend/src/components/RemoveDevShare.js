import React from 'react';
import {withVisible} from '../components-ui/withVisible';
import {Button, Divider, Icon, Modal} from 'semantic-ui-react';

class RemoveDevShareComponent extends React.Component {

  constructor(props) {
    super(props);

    this.state = this.getDefaultState();
  }

  getDefaultState = () => ({
    isSaving: false,
    transactionError: ''
  });

  onSubmit = async e => {
    this.setState({isSaving: true});
    try {
      await this.props.onSubmit();
      this.props.onHide();
    } catch (e) {
      this.setState({transactionError: e.message})
    }
    this.setState({isSaving: false});
  };

  onShowHandle = () => {
    const {onShow} = this.props;
    this.setState(this.getDefaultState());
    onShow();
  };

  renderTransactionError = () => {
    if (!this.state.transactionError) {
      return null
    }

    return (
      <div>
        <Divider/>
        <div style={{color: 'red'}}>
          {this.state.transactionError}
        </div>
      </div>
    )
  };

  render() {
    const {isVisible, onHide} = this.props;
    const {isSaving} = this.state;

    return (
      <span>
        <Icon link name='remove' size='large' onClick={this.onShowHandle}/>
        <Modal size="tiny" open={isVisible} onClose={isSaving ? () => {} : onHide}>
          <Modal.Header>Remove Share Holder</Modal.Header>
            <Modal.Content>
              You are removing Share Holder. Are you sure?
              <Divider />
              <span>PS: Please be patient this may take a while...</span>
              {this.renderTransactionError()}
            </Modal.Content>
            <Modal.Actions>
              <Button negative disabled={isSaving} onClick={onHide}>Cancel</Button>
              <Button positive loading={isSaving} disabled={isSaving} icon='checkmark' labelPosition='right'
                      content='Remove' onClick={this.onSubmit}/>
            </Modal.Actions>
          </Modal>
      </span>
    );
  }
}

export const RemoveDevShare = withVisible(RemoveDevShareComponent);
