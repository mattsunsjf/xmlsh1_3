# core_varseq.xsh
# test variable sequences

if [ -z "$TMPDIR" -o ! -d "$TMPDIR" ] ; then
	echo Temp directory required TMPDIR: $TMPDIR
	exit 1;
fi

# Test that a empty string counts as 1 element
_var=""
echo empty string count <[ count($_var) ]> should be 1
unset _var
# empty sequence
_var=()
echo empty sequence count <[ count($_var) ]> should be 0

# string sequences
_var=($_var a b)
echo appended "a" "b" count <[ count($_var) ]> should be 2

# Mixed and flattened
_var=($_var <[ 1,2 , <foo/> ]> )
echo mixed sequence count <[ count($_var) ]> should be 5

mkdir $TMPDIR/_varseq || exit 1
cd $TMPDIR/_varseq || exit 1

echo foo > foo
echo bar > bar
unset _var
_var=(*)

echo $_var should be bar foo
cd ..
rm -rf $TMPDIR/_varseq
exit 0



