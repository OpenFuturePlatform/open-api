import {SET_SCAFFOLD_TEMPLATES} from "../actions/types";

const scaffoldTemplates = (state = [], action) => {
  switch (action.type) {
    case SET_SCAFFOLD_TEMPLATES:
      return [...state, ...action.payload];
    default:
      return state;
  }
};

export default scaffoldTemplates;
