'use strict';

const NQPObject = require('./nqp-object.js');
const Null = require('./null.js');

const HashIter = require('./hash-iter.js');

const sixmodel = require('./sixmodel.js');

class Hash extends NQPObject {
  constructor() {
    super();
    this.$$SC = undefined;
    this.content = new Map();
  }

  $$bindkey(key, value) {
    this.content.set(key, value);
    if (this.$$SC !== undefined) this.$$scwb();
    return value;
  }

  $$atkey(key) {
    return this.content.has(key) ? this.content.get(key) : Null;
  }

  $$existskey(key) {
    return this.content.has(key);
  }

  $$deletekey(key) {
    if (this.$$SC !== undefined) this.$$scwb();
    this.content.delete(key);
    return this;
  }

  $$clone() {
    const clone = new Hash();
    this.content.forEach(function(value, key, map) {
      clone.content.set(key, value);
    });
    return clone;
  }

  $$elems() {
    return this.content.size;
  }

  $$numify() {
    return this.$$elems();
  }

  $$toObject() {
    const ret = {};
    this.content.forEach(function(value, key, map) {
      ret[key] = value;
    });
    return ret;
  }

  $$toBool() {
    return this.content.size == 0 ? 0 : 1;
  }

  $$iterator() {
    return new HashIter(this);
  }

  $$istype(ctx, type) {
    return 0;
  }

  $$can(ctx, name) {
    return 0;
  }
};

Hash.prototype.$$scwb = sixmodel.scwb;

module.exports = Hash;
