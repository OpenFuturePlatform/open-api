import React from 'react';
import {connect} from 'react-redux';
import {Button} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {saveTemplate} from '../actions/contract-templates';

const TemplateSaveView = (props) => {

  const handleOnSaveTemplate = async (e) => {
    const {actions, fields} = props;
    e.preventDefault();

    await actions.saveTemplate(fields);
  };

  return (
    <Button secondary onClick={handleOnSaveTemplate}
            style={{
              marginBottom: '10px',
              marginRight: '15px',
            }}>
      Save Template
    </Button>
  );
};

const mapStateToProps = (state, {fields}) => ({fields});

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({saveTemplate}, dispatch),
});


export const TemplateSave = connect(mapStateToProps, mapDispatchToProps)(TemplateSaveView);
