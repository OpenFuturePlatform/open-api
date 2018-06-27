import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { fetchKeys, generateKey, removeKey } from '../actions/keys';
import { Table } from '../components-ui/Table';
import { KeyGenerate } from '../components/KeyGenerate';
import { Status } from '../components-ui/Status';
import { KeyRemove } from '../components/KeyRemove';
import { formatDate } from '../utils/format-date';

const getColumns = onRemove => [
  {
    Header: 'Key',
    accessor: 'value',
    sortable: false
  },
  {
    Header: 'Status',
    maxWidth: 200,
    accessor: 'enabled',
    Cell: ({ value }) => <Status value={value} />,
    sortable: false
  },
  {
    Header: 'Expired Date',
    maxWidth: 200,
    accessor: 'expiredDate',
    Cell: ({ value }) => formatDate(value),
    sortable: false
  },
  {
    maxWidth: 70,
    accessor: 'enabled',
    Cell: ({ value, original }) => value && <KeyRemove onSubmit={() => onRemove(original.value)} />,
    sortable: false
  }
];

class Keys extends Component {
  componentDidMount() {
    this.props.actions.fetchKeys();
  }

  render() {
    const { keys, actions } = this.props;
    const columns = getColumns(keyValue => actions.removeKey(keyValue));

    return (
      <div>
        <KeyGenerate onSubmit={key => actions.generateKey(key)} />
        <Table data={keys} columns={columns} />
      </div>
    );
  }
}

const mapStateToProps = ({ keys }) => ({ keys });

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators({ fetchKeys, generateKey, removeKey }, dispatch)
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Keys);
